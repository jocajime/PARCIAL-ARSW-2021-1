package eci.arsw.covidanalyzer.persistence.impl;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import eci.arsw.covidanalyzer.persistence.ICovidPersistence;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("covidPersistence")
public class covidPersisntece implements ICovidPersistence {

    private final Map<Result, ResultType> results=new HashMap<> ();
    private final Map<UUID, Integer> resultsMultiple= new HashMap<> ();

    @Override
    public void saveResult(Result r, ResultType rt) {
        results.put (r,rt);
    }

    @Override
    public Set<Result> getResult(ResultType type) {
        Set<Result> resultados = new HashSet<> ();
        for(Result r: results.keySet ()){
            if(results.get(r).equals (type)){
                resultados.add (r);
            }
        }
        return resultados;
    }

    @Override
    public boolean updatePerson(UUID id, ResultType type) {
        boolean res = false;
        for(Result r: results.keySet ()){
            if(r.getId ().equals (id)){
                results.put (r,type);
                if(resultsMultiple.containsKey (id)){
                    int numberTest = resultsMultiple.get(id);
                    resultsMultiple.remove (id);
                    resultsMultiple.put (id,numberTest++);
                }else {
                    resultsMultiple.put (id,2);
                }
                res=true;
            }
        }
        return res;
    }

}
