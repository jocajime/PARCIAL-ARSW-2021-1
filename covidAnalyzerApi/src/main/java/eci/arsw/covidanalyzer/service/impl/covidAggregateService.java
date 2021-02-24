package eci.arsw.covidanalyzer.service.impl;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import eci.arsw.covidanalyzer.persistence.ICovidPersistence;
import eci.arsw.covidanalyzer.service.ICovidAggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service("covidAggregateService")
public class covidAggregateService implements ICovidAggregateService {

    @Autowired
    @Qualifier("covidPersistence")
    ICovidPersistence persistence;

    @Override
    public boolean aggregateResult(Result result, ResultType type) {
        persistence.saveResult(result,type);
        return true;
    }

    @Override
    public Set<Result> getResult(ResultType type) {
        return persistence.getResult(type);
    }

    @Override
    public boolean upsertPersonWithMultipleTests(UUID id, ResultType type) {
        try {
            return persistence.updatePerson(id,type);
        }catch (Exception e){
            return false;
        }
    }
}
