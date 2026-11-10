package com.javengers.Evaluation;

import com.javengers.Evaluation.Strorage.*;
import com.javengers.Evaluation.Generator.*;
import com.javengers.Evaluation.RestApiEvaluation.*;
import com.javengers.Evaluation.MathEvaluator.*;

public class EvaluationMain {
    public static void main(String[] args) {


    String centralUrl= "http://localhost:8080/api/v1/central";
    int EvaluationPort= 8081;

    RandomGenerator randomGenerator= new RandomGenerator();
    Database_evaluation databaseEvaluation= new Database_evaluation();
    MathEvaluator mathEvaluator= new MathEvaluator(databaseEvaluation);

    RestClient restClient= new RestClient(centralUrl);
    RestController restController= new RestController(restClient, databaseEvaluation, randomGenerator);

    restController.start(EvaluationPort);



}

}