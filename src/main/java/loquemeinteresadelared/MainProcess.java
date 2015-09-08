package loquemeinteresadelared;

import loquemeinteresadelared.conf.AppConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class MainProcess {

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		ctx.getBean(LoadCsv.class).process();
		((AbstractApplicationContext)ctx).close();
	}

}
