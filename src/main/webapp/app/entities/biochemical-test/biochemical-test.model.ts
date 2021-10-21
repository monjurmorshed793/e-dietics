import { IMeasurementType } from 'app/entities/measurement-type/measurement-type.model';

export interface IBiochemicalTest {
  id?: string;
  testName?: string | null;
  description?: string | null;
  defaultMeasurementType?: IMeasurementType | null;
}

export class BiochemicalTest implements IBiochemicalTest {
  constructor(
    public id?: string,
    public testName?: string | null,
    public description?: string | null,
    public defaultMeasurementType?: IMeasurementType | null
  ) {}
}

export function getBiochemicalTestIdentifier(biochemicalTest: IBiochemicalTest): string | undefined {
  return biochemicalTest.id;
}
