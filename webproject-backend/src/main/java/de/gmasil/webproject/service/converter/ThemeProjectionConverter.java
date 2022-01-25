package de.gmasil.webproject.service.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.newprojection.ThemeProjectionNEW;

@Service
public class ThemeProjectionConverter implements Converter<Theme, ThemeProjectionNEW> {

    @Override
    public ThemeProjectionNEW convert(Theme theme) {
        return new ThemeProjectionNEW(theme);
    }
}
