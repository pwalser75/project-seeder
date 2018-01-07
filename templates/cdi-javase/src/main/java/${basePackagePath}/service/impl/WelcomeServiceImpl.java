package ${basePackage}.service.impl;

import ${basePackage}.service.WelcomeService;

/**
 * Implementation of the {@link WelcomeService}
 */
public class WelcomeServiceImpl implements WelcomeService {

    @Override
    public String welcome() {
        return "Hello World";
    }
}
