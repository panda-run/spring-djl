package org.pandarun.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pandarun.service.ComputerVisionService;
import org.springframework.stereotype.Service;


/**
 * @author James Zow
 * @version 1.0
 * @description: TODO
 * @date 2021/11/1 14:07
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ComputerVisionServiceImpl implements ComputerVisionService {

    private final Classification classification;

    private final PneumoniaDetection pneumoniaDetection;

    @Override
    public String imageRecognition(String path) {
        String result = null;
        try {
            result = classification.predict(path);
        }catch (Exception e){
            log.error("图像识别推理异常：{}", e);
            return result;
        }
        return result;
    }

    @Override
    public String pneumoniaDetection(String path) {
        String result = null;
        try {
            result = pneumoniaDetection.predict(path);
        }catch (Exception e){
            log.error("肺炎检测推理异常：{}", e);
            return result;
        }
        return result;
    }
}
