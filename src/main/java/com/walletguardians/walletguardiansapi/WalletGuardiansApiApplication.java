package com.walletguardians.walletguardiansapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WalletGuardiansApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletGuardiansApiApplication.class, args);
	}

}
