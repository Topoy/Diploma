package main.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "blog")
@ConstructorBinding
public class ImmutableCredentials
{
    private final String title;
    private final String subtitle;
    private final String phone;
    private final String email;
    private final String copyright;
    private final String copyrightFrom;

    public ImmutableCredentials(String title, String subtitle, String phone, String email, String copyright, String copyrightFrom)
    {
        this.title = title;
        this.subtitle = subtitle;
        this.phone = phone;
        this.email = email;
        this.copyright = copyright;
        this.copyrightFrom = copyrightFrom;
    }

    public String getTitle()
    {return title;}
    public String getSubtitle() {
        return subtitle;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getCopyrightFrom() {
        return copyrightFrom;
    }
}
