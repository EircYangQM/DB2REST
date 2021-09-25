package com.db2rest.server.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class fastJsonConfig extends WebMvcConfigurationSupport {
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    FastJsonConfig config = new FastJsonConfig();
    config.setSerializerFeatures(
        SerializerFeature.WriteMapNullValue,
        SerializerFeature.WriteNullStringAsEmpty,
        SerializerFeature.WriteNullNumberAsZero,
        SerializerFeature.WriteNullListAsEmpty,
        SerializerFeature.WriteNullBooleanAsFalse,
        SerializerFeature.DisableCircularReferenceDetect);

    converter.setFastJsonConfig(config);
    converter.setDefaultCharset(StandardCharsets.UTF_8);
    List<MediaType> mediaTypeList = new ArrayList<>();
    mediaTypeList.add(MediaType.APPLICATION_JSON);
    converter.setSupportedMediaTypes(mediaTypeList);
    converters.add(converter);
  }
}
