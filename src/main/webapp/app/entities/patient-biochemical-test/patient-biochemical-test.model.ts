import { IPatient } from 'app/entities/patient/patient.model';
import { IBiochemicalTest } from 'app/entities/biochemical-test/biochemical-test.model';
import { IMeasurementType } from 'app/entities/measurement-type/measurement-type.model';

export interface IPatientBiochemicalTest {
  id?: string;
  other?: string | null;
  value?: number | null;
  patient?: IPatient | null;
  biochemicalTest?: IBiochemicalTest | null;
  measurementType?: IMeasurementType | null;
}

export class PatientBiochemicalTest implements IPatientBiochemicalTest {
  constructor(
    public id?: string,
    public other?: string | null,
    public value?: number | null,
    public patient?: IPatient | null,
    public biochemicalTest?: IBiochemicalTest | null,
    public measurementType?: IMeasurementType | null
  ) {}
}

export function getPatientBiochemicalTestIdentifier(patientBiochemicalTest: IPatientBiochemicalTest): string | undefined {
  return patientBiochemicalTest.id;
}
