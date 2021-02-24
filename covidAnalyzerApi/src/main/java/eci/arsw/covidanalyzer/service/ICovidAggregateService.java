package eci.arsw.covidanalyzer.service;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;

import java.util.Set;
import java.util.UUID;

public interface ICovidAggregateService {

    /**
     * Add a new result into the specified result type storage.
     *
     * @param result
     * @param type
     * @return
     */
    boolean aggregateResult(Result result, ResultType type);

    /**
     * Get all the results for the specified result type.
     *
     * @param type
     * @return
     */
    Set<Result> getResult(ResultType type);

    /**
     *  @param id
     * @param type
     * @return
     */
    boolean upsertPersonWithMultipleTests(UUID id, ResultType type);


}
