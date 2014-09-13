package com.blueberry.presentation.web

import com.blueberry.framework.presentation.PresentationWebController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.app.ApplicationInstanceInfo
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.ReflectionUtils
import org.springframework.web.bind.annotation.RequestMapping

import javax.sql.DataSource
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/12/14
 */
@Controller
@RequestMapping("/web")
@Profile("cloud")
class SystemWebController extends PresentationWebController {

    @Autowired(required = false) DataSource dataSource;
    @Autowired ApplicationInstanceInfo instanceInfo;

    @RequestMapping("/system")
    public String home(Model model) {
        Map<Class<?>, String> services = new LinkedHashMap<Class<?>, String>();
        services.put(dataSource.getClass(), toString(dataSource));
        model.addAttribute("services", services.entrySet());
        model.addAttribute("instanceInfo", instanceInfo);

        return "home";
    }

    private String toString(DataSource dataSource) {
        if (dataSource == null) {
            return "<none>";
        } else {
            try {
                Field urlField = ReflectionUtils.findField(dataSource.getClass(), "url");
                ReflectionUtils.makeAccessible(urlField);
                return stripCredentials((String) urlField.get(dataSource));
            } catch (Exception fe) {
                try {
                    Method urlMethod = ReflectionUtils.findMethod(dataSource.getClass(), "getUrl");
                    ReflectionUtils.makeAccessible(urlMethod);
                    return stripCredentials((String) urlMethod.invoke(dataSource, (Object[])null));
                } catch (Exception me){
                    return "<unknown> " + dataSource.getClass();
                }
            }
        }
    }

    private String stripCredentials(String urlString) {
        try {
            if (urlString.startsWith("jdbc:")) {
                urlString = urlString.substring("jdbc:".length());
            }
            URI url = new URI(urlString);
            return new URI(url.getScheme(), null, url.getHost(), url.getPort(), url.getPath(), null, null).toString();
        }
        catch (URISyntaxException e) {
            System.out.println(e);
            return "<bad url> " + urlString;
        }
    }
}
