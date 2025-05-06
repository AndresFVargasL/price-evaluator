@acceptanceTest
Feature: Pruebas de aceptación al endpoint REST del servicio de tarifas aplicables

  Background:
    * url baseUrl

  Scenario: Test 1 - Petición a las 10:00 del día 14 para el producto 35455 y brand 1 (ZARA)
    Given path '/api/prices'
    And param applicationDate = '2020-06-14T10:00:00'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response == { id: 1, brandId: 1, startDate: '2020-06-14T00:00:00', endDate: '2020-12-31T23:59:59', priceList: 1, productId: 35455, priority: 0, price: 35.50, curr: 'EUR' }

  Scenario: Test 2 - Petición a las 16:00 del día 14 para el producto 35455 y brand 1 (ZARA)
    Given path '/api/prices'
    And param applicationDate = '2020-06-14T16:00:00'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response == { id: 2, brandId: 1, startDate: '2020-06-14T15:00:00', endDate: '2020-06-14T18:30:00', priceList: 2, productId: 35455, priority: 1, price: 25.45, curr: 'EUR' }

  Scenario: Test 3 - Petición a las 21:00 del día 14 para el producto 35455 y brand 1 (ZARA)
    Given path '/api/prices'
    And param applicationDate = '2020-06-14T21:00:00'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response == { id: 1, brandId: 1, startDate: '2020-06-14T00:00:00', endDate: '2020-12-31T23:59:59', priceList: 1, productId: 35455, priority: 0, price: 35.50, curr: 'EUR' }

  Scenario: Test 4 - Petición a las 10:00 del día 15 para el producto 35455 y brand 1 (ZARA)
    Given path '/api/prices'
    And param applicationDate = '2020-06-15T10:00:00'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response == { id: 3, brandId: 1, startDate: '2020-06-15T00:00:00', endDate: '2020-06-15T11:00:00', priceList: 3, productId: 35455, priority: 1, price: 30.50, curr: 'EUR' }

  Scenario: Test 5 - Petición a las 21:00 del día 16 para el producto 35455 y brand 1 (ZARA)
    Given path '/api/prices'
    And param applicationDate = '2020-06-16T21:00:00'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response == { id: 4, brandId: 1, startDate: '2020-06-15T16:00:00', endDate: '2020-12-31T23:59:59', priceList: 4, productId: 35455, priority: 1, price: 38.95, curr: 'EUR' }
