package edu.pet;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration // так как конфигурация, спринг сам найдет этот бин и добавит в контекст
public class TrackerConfiguration implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        PathPatternParser parser = new PathPatternParser(); // свой парсер пути
        parser.setCaseSensitive(false); // с настройкой игнорирования кейса
        configurer.setPatternParser(parser); // ставим в качестве текущего
    }
}
