import {Strategy} from './Strategy';
import {Direction} from './Direction';

export interface IRow {
    data: number[],
    strategyRandom: string,
    expectedResult: number[]
    directionRandom: string
}
