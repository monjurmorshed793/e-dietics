export interface IMeasurementType {
  id?: string;
  label?: string;
  description?: string | null;
}

export class MeasurementType implements IMeasurementType {
  constructor(public id?: string, public label?: string, public description?: string | null) {}
}

export function getMeasurementTypeIdentifier(measurementType: IMeasurementType): string | undefined {
  return measurementType.id;
}
