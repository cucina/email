package org.cucina.email;

import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.cucina.email.repository.EmailTemplate;
import org.cucina.email.repository.TemplateRepository;
import org.cucina.email.service.AbstractEmailHandler;
import org.cucina.email.service.MimeMessagePreparatorFactory;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@RestController
public class HttpEmailHandler
    extends AbstractEmailHandler {
    @Autowired
    private TemplateRepository templateRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @RequestMapping(value = "/addTemplate", method = RequestMethod.GET)
    @ResponseBody
    public String addGet() {
        return "Please post file to the same URL";
    }

    /**
     * JAVADOC Method Level Comments
     */
    @RequestMapping(value = "/addTemplate", method = RequestMethod.POST)
    @ResponseBody
    public String addTemplate(@RequestParam("name")
    String name, @RequestParam("locale")
    Locale locale, @RequestParam("file")
    MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String body = new String(bytes, StandardCharsets.UTF_8);
                EmailTemplate et = new EmailTemplate();

                et.setBody(body);
                et.setName(name + ((locale == null) ? "" : ("_" + locale.toString())) +
                    MimeMessagePreparatorFactory.TEMPLATE_SUFFIX);
                et.setLastModified(new Date());
                templateRepository.save(et);

                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        }

        return "You failed to upload " + name + " because the file was empty.";
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param emailDto JAVADOC.
     */
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public void sendEmail(@RequestBody
    EmailDto dto) {
        sendEmail(dto);
    }
}
