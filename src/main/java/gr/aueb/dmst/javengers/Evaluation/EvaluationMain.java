package gr.aueb.dmst.javengers.Evaluation;

import gr.aueb.dmst.javengers.Evaluation.Generator.RandomGenerator;
import gr.aueb.dmst.javengers.Evaluation.MathEvaluator.MathEvaluator;
import gr.aueb.dmst.javengers.Evaluation.Storage.DataStorage;

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

    

