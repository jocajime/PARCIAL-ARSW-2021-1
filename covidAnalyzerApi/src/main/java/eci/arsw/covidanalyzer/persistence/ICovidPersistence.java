package eci.arsw.covidanalyzer.persistence;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;

import java.util.Set;
import java.util.UUID;

public interface ICovidPersistence {

    void saveResult(Result r, ResultType rt);

    Set<Result> getResult(ResultType type);

    boolean updatePerson(UUID id, ResultType type);
}
