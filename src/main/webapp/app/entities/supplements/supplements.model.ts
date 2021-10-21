import { IPatient } from 'app/entities/patient/patient.model';

export interface ISupplements {
  id?: string;
  name?: string | null;
  description?: string | null;
  patients?: IPatient[] | null;
}

export class Supplements implements ISupplements {
  constructor(public id?: string, public name?: string | null, public description?: string | null, public patients?: IPatient[] | null) {}
}

export function getSupplementsIdentifier(supplements: ISupplements): string | undefined {
  return supplements.id;
}
