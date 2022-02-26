package br.com.sw2you.realmeet.config;

import br.com.sw2you.realmeet.filter.VerifyApiKeyFilter;
import br.com.sw2you.realmeet.repository.ClientRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    private ClientRepository clientRepository;

    public FilterConfiguration(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Bean
    public FilterRegistrationBean<VerifyApiKeyFilter> verifyApiKeyFilter() {
        var filterFilterRegistrationBean = new FilterRegistrationBean<VerifyApiKeyFilter>();
        filterFilterRegistrationBean.setFilter(new VerifyApiKeyFilter(clientRepository));
        filterFilterRegistrationBean.addUrlPatterns("/rooms/*", "/allocations/*");
        return filterFilterRegistrationBean;
    }
}
