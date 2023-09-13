package ua.com.foxminded;

import ua.com.foxminded.repository.EmploeeRepository;
import ua.com.foxminded.repository.EmploeeRepositoryConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new EmploeeRepository(EmploeeRepositoryConfig.getH2DataSource());
    }
}