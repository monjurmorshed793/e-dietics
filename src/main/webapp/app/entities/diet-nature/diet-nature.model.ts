import { IPatient } from 'app/entities/patient/patient.model';

export interface IDietNature {
  id?: string;
  name?: string | null;
  description?: string | null;
  patients?: IPatient[] | null;
}

export class DietNature implements IDietNature {
  constructor(public id?: string, public name?: string | null, public description?: string | null, public patients?: IPatient[] | null) {}
}

export function getDietNatureIdentifier(dietNature: IDietNature): string | undefined {
  return dietNature.id;
}
