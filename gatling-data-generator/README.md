# Gatling data generator

Simple tool written using `Node.js` and `TypeScript` to generate CSV feed files for Gatling test of Sorting API.

### Usage

> NPM package manager is required to run this program.


- Install dependencies 
    ```shell
    npm i
    ````
- Generate file with random data to feed Gatling `LargeNumberSetSimulation` and `MediumNumberSetSimulation`.
    ```shell
    npm start <NUMBER_OF_EXAMPLES> <COUNT_OF_NUMBERS_IN_ARRAY> <RANGE_GENERATION_START> <RANGE_GENERATION_END>
    ```
### Examples

This tool is not perfect and is meant to do its job not to be a masterpiece. Do not expect good input params validation etc.

> Range must start from at least one and end at most at the Java Integer type max value.

  ```shell
  npm run start 50 500000 1 2147483647
  ```
  **Description**: *Generate 50 examples where array to sort has 500 000 elements and values are from range 1 - 2147483647*
  ```shell
  npm run start 100 50000 1 2147483647
  ```
**Description**: *Generate 100 examples where array to sort has 50 000 elements and values are from range 1 - 2147483647*

### Limitations

Used under the hood library for parsing JavaScript objects to CSV has limitations related to the maximum string length. If You generate too much data rows or with too big table the script will fail due to problem with conversion to CSV. 
