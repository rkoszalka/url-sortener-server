package com.koszalka.shortener.rest.api;

import com.koszalka.shortener.persistence.dto.ShortenerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(path = "/shortener")
public interface  ShortenerAPI {

    @RequestMapping(path = "/{urlString}",method = RequestMethod.GET)
    ResponseEntity<ShortenerDTO> getUrlByString(@PathVariable("urlString") String urlString);

    @RequestMapping(path = "/new/{urlString}",method = RequestMethod.POST)
    ResponseEntity<ShortenerDTO> postNewUrlString(@PathVariable("urlString") String urlString);
}
