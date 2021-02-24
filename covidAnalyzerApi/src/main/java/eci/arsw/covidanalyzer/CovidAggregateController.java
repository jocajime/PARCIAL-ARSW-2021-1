package eci.arsw.covidanalyzer;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import eci.arsw.covidanalyzer.service.ICovidAggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class CovidAggregateController {

    @Autowired
    @Qualifier("covidAggregateService")
    ICovidAggregateService covidAggregateService;

    @RequestMapping(value = "/covid/result/true-positive", method = RequestMethod.POST)
    public ResponseEntity addTruePositiveResult(@RequestBody Result result) {

        if(covidAggregateService.aggregateResult(result, ResultType.TRUE_POSITIVE)){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<> ("nose pudo crear el resultado", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/covid/result/true-negative", method = RequestMethod.POST)
    public ResponseEntity addTrueNegativeResult(@RequestBody Result result) {
        if(covidAggregateService.aggregateResult(result, ResultType.TRUE_NEGATIVE)){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<> ("nose pudo crear el resultado", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/covid/result/false-positive", method = RequestMethod.POST)
    public ResponseEntity addFalsePositiveResult(@RequestBody Result result) {
        if(covidAggregateService.aggregateResult(result, ResultType.FALSE_POSITIVE)){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<> ("nose pudo crear el resultado", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/covid/result/false-negative", method = RequestMethod.POST)
    public ResponseEntity addFalseNegativeResult(@RequestBody Result result) {
        try{
            if(covidAggregateService.aggregateResult(result, ResultType.FALSE_NEGATIVE)){
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity<> ("nose pudo crear el resultado", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            Logger.getLogger (CovidAggregateController.class.getName ()).log (Level.SEVERE, null, e);
            return new ResponseEntity<> ("nose pudo crear el resultado", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/covid/result/true-positive", method = RequestMethod.GET)
    public ResponseEntity getTruePositiveResult() {
        try{
            return new ResponseEntity<>(covidAggregateService.getResult(ResultType.TRUE_POSITIVE),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger (CovidAggregateController.class.getName ()).log (Level.SEVERE, null, e);
            return new ResponseEntity<> ("nose pudo encontrar resultados", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/covid/result/true-negative", method = RequestMethod.GET)
    public ResponseEntity getTrueNegativeResult() {
        try{
            return new ResponseEntity<>(covidAggregateService.getResult(ResultType.TRUE_NEGATIVE),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger (CovidAggregateController.class.getName ()).log (Level.SEVERE, null, e);
            return new ResponseEntity<> ("nose pudo encontrar resultados", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/covid/result/false-positive", method = RequestMethod.GET)
    public ResponseEntity getFalsePositiveResult() {
        try{
            return new ResponseEntity<>(covidAggregateService.getResult(ResultType.FALSE_POSITIVE),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger (CovidAggregateController.class.getName ()).log (Level.SEVERE, null, e);
            return new ResponseEntity<> ("nose pudo encontrar resultados", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/covid/result/false-negative", method = RequestMethod.GET)
    public ResponseEntity getFalseNegativeResult() {
        try{
            return new ResponseEntity<>(covidAggregateService.getResult(ResultType.FALSE_NEGATIVE),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger (CovidAggregateController.class.getName ()).log (Level.SEVERE, null, e);
            return new ResponseEntity<> ("nose pudo encontrar resultados", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getPage() {
            return new ResponseEntity<>("hello",HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/covid/result/persona/{id}", method = RequestMethod.PUT)
    public ResponseEntity savePersonaWithMultipleTests(@RequestBody ResultType type,@PathVariable UUID id) {
        if(covidAggregateService.upsertPersonWithMultipleTests (id,type)){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<> ("nose pudo crear el resultado", HttpStatus.NOT_FOUND);
        }
    }
    
}