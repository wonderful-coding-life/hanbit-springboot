package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringCafe implements ApplicationRunner {
    @Autowired
    private Barista barista;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        barista.makeCoffees();

        // 옵션형 인자 --name, --age
        // java -jar app.jar --name=hong --age=20 file1.txt file2.txt
        List<String> optionValues = args.getOptionValues("name");
        if (optionValues != null) {
            for (String optionValue : optionValues) {
                System.out.println("option arg(name) = " + optionValue);
            }
        }

        // 일반형 인자 file1.txt file2.txt
        // java -jar app.jar --name=hong --age=20 file1.txt file2.txt
        List<String> nonOptionArgs = args.getNonOptionArgs();
        for (String nonOptionArg : nonOptionArgs) {
            System.out.println("non option arg = " + nonOptionArg);
        }
    }
}
