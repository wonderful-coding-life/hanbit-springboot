package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
        // SpringApplication.run() 에 전달한 클래스는
        // 주로 애노테이션과 패키지 위치 같은 설정 정보를 분석하는 용도로 사용된다.
        // 일반 메서드를 자동으로 호출하지는 않으며,
        // @Bean 메서드가 있는 경우에는 Bean 생성 과정에서 해당 메서드를 호출한다.
		var context = SpringApplication.run(DemoApplication.class, args);

        // ApplicationContext를 통해 Bean을 직접 조회할 수도 있다.
        // 다만 실무에서는 컨테이너에 직접 접근하기보다는,
        // 생성자 주입과 같은 의존성 주입(DI) 방식을 통해 Bean을 사용하는 경우가 대부분이다.
        var barista = context.getBean(Barista.class);
        barista.makeCoffees();

        // ApplicationContext 내부에 등록된 Bean 개수를 조회한다.
        System.out.println("beans count = " + context.getBeanDefinitionCount());

        // ApplicationContext에 등록된 모든 Bean 이름을 순회하며 출력한다.
        for (String beanName : context.getBeanDefinitionNames()) {
            System.out.println("bean " + beanName);
        }
	}
}
