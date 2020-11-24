/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.djl.examples.inference;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.nlp.qa.QAInput;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用BertQA进行推理的一个例子.
 *
 * <p>详情 如下:
 *
 * <ul>
 *   <li>the <a href="https://github.com/awslabs/djl/blob/master/jupyter/BERTQA.ipynb">jupyter
 *       demo</a> 关于Bert的更多信息.
 *   <li>the <a
 *       href="https://github.com/awslabs/djl/blob/master/examples/docs/BERT_question_and_answer.md">文档</a>
 *       有关运行此示例的信息.
 * </ul>
 */
public final class BertQaInference {

    private static final Logger logger = LoggerFactory.getLogger(BertQaInference.class);

    private BertQaInference() {}

    public static void main(String[] args) throws IOException, TranslateException, ModelException {
        String answer = BertQaInference.predict();
        logger.info("回答: {}", answer);
    }

    public static String predict() throws IOException, TranslateException, ModelException {
        String question = "When did BBC Japan start broadcasting?";
        String paragraph =
                "BBC Japan was a general entertainment Channel.\n"
                        + "Which operated between December 2004 and April 2006.\n"
                        + "It ceased operations after its Japanese distributor folded.";

        QAInput input = new QAInput(question, paragraph);
        logger.info("段落: {}", input.getParagraph());
        logger.info("问题: {}", input.getQuestion());

        Criteria<QAInput, String> criteria =
                Criteria.builder()
                        .optApplication(Application.NLP.QUESTION_ANSWER)
                        .setTypes(QAInput.class, String.class)
                        .optFilter("backbone", "bert")
                        .optProgress(new ProgressBar())
                        .build();

        try (ZooModel<QAInput, String> model = ModelZoo.loadModel(criteria)) {
            try (Predictor<QAInput, String> predictor = model.newPredictor()) {
                return predictor.predict(input);
            }
        }
    }
}
