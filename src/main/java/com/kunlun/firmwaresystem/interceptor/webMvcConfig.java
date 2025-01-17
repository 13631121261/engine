package com.kunlun.firmwaresystem.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;


@Configuration
@EnableSwagger2
public class webMvcConfig implements WebMvcConfigurer {
 //   @Bean
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(" com.kunlun.firmwaresystem.controller_user.Api"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        //网站描述
                        .description("接口文档的描述信息")
                        .title("定位引擎接口文档")
                        //联系人信息
                        .contact(new Contact("andesen","KL","929620555@qq.com"))
                        //版本
                        .version("v1.0")
                        .license("Apache2.0")
                        .build());
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        println("不执行");
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler()
                .addResourceLocations("classpath:/templates/")
                .addResourceLocations("classpath:/static/");
      //  super.addResourceHandlers(registry);

    }
}