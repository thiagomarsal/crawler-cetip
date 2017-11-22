package br.com.clubedoporquinho.ws;

import br.com.clubedoporquinho.service.WebScraping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlerCetipRest {

    @Autowired
    private WebScraping webScraping;

    @RequestMapping(method = RequestMethod.GET, value = "/crawler/cetip/{cpf}/{senha}/{codInstituicao}")
    public void cetip(@PathVariable String cpf, @PathVariable String senha, @PathVariable String codInstituicao) {
        webScraping.start(cpf, senha, codInstituicao);
    }
}
