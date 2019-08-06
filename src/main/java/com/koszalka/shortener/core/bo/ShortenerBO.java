package com.koszalka.shortener.core.bo;

import com.koszalka.shortener.utils.UrlShortenerUtil;
import com.koszalka.shortener.utils.UrlShortenerValidationUtil;
import com.koszalka.shortener.persistence.entities.ShortenerEntity;
import com.koszalka.shortener.persistence.repositories.ShortenerRepository;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortenerBO {

    private final static String ALREADY_EXISTS = "exists";
    private final static String SAVED = "saved";
    private final static String BAD_REQUEST = "bad_request";
    private final ShortenerRepository shortenerRepository;

    @Autowired
    public ShortenerBO(ShortenerRepository shortenerRepository) {
        this.shortenerRepository = shortenerRepository;
    }

    public String saveOne(ShortenerEntity entity)  {
        if (validateURL(entity.getOriginal())) {
            UrlShortenerUtil urlShortenerUtil = new UrlShortenerUtil();
            int stringId = urlShortenerUtil.tinyUrlToId(entity.getOriginal());
            String hash = urlShortenerUtil.idToTinyUrl(stringId);
            if (verifyIfHashAlreadyExist(hash) > 0) {
                return ALREADY_EXISTS;
            }
            entity.setHash(hash);
            shortenerRepository.save(entity);
            return SAVED;
        }
        return BAD_REQUEST;
    }

    public ShortenerEntity getUrlFromHash(String hash) {
        ShortenerEntity entity = shortenerRepository.getOriginalUrlFromHash(hash);
        return entity;
    }

    private boolean validateURL(String url) {
        UrlShortenerValidationUtil shortener = new UrlShortenerValidationUtil(url);
        return shortener.validateURL();
    }

    private Long verifyIfHashAlreadyExist(String hash) {
        return shortenerRepository.verifyIfHashAlreadyExist(hash);
    }

    public void send301Redirect(HttpServletResponse response, String newUrl) {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", newUrl);
        response.setHeader("Connection", "close");
    }
}
