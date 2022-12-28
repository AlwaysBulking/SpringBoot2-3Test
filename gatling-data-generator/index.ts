import {IRow} from './IRow';
import {Strategy} from './Strategy';
import {Direction} from './Direction';
import ObjectsToCsv = require('objects-to-csv');

const argv: string[] = process.argv.slice(2);

async function generate() {
  if (argv.length !== 4) {
    console.error('Invalid number of parameters.');
    process.exit(1);
  }
  const numberOfRows = Number(argv[0]);
  const arrayNumberSize = Number(argv[1]);
  const minNumber = Number(argv[2]);
  const maxNumber = Number(argv[3]);
  // TODO Improve validation messages
  if (numberOfRows < 1 || arrayNumberSize < 1 || minNumber < 1 || maxNumber < 1 || minNumber >= maxNumber) {
    console.error('Invalid param value.');
    process.exit(2);
  }
  const rows: IRow[] = [];
  for (let i = 0; i < numberOfRows; i++) {
    const singleRow = generateSingleRow(arrayNumberSize, maxNumber, minNumber);
    rows.push(singleRow);
  }

  const csv = new ObjectsToCsv(rows);
  await csv.toDisk('./data.csv');
}


function generateSingleRow(arrayNumberSize: number, maxNumber: number, minNumber: number): IRow {
  const numbersToBeSorted: number[] = [];
  for (let j = 0; j < arrayNumberSize; j++) {
    numbersToBeSorted.push(randomNumber(minNumber, maxNumber));
  }
  const direction = randomDirection();
  const strategy = randomStrategy();
  const numbersToBeSortedCopy: number[] = [...numbersToBeSorted];
  return {
    data: shuffleArray(numbersToBeSorted),
    directionRandom: Direction[direction],
    strategyRandom: Strategy[strategy],
    expectedResult: direction == Direction.ASCENDING ? numbersToBeSortedCopy.sort((a, b) => a - b) : numbersToBeSortedCopy.sort((a, b) => b - a),
  };
}

function shuffleArray(array): number[] {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
  return array;
}

function randomNumber(min: number, max: number): number {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function randomStrategy(): Strategy {
  const strategyNumber = randomNumber(1, 3);
  switch (strategyNumber) {
    case 1:
      return Strategy.MERGE_SORT;
    case 2:
      return Strategy.QUICKSORT;
    case 3:
      return Strategy.HEAP_SORT;
  }
}

function randomDirection(): Direction {
  const strategyNumber = randomNumber(1, 2);
  switch (strategyNumber) {
    case 1:
      return Direction.ASCENDING;
    case 2:
      return Direction.DESCENDING;
  }
}

generate();
