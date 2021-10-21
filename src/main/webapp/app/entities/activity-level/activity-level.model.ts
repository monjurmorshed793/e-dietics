export interface IActivityLevel {
  id?: string;
  order?: number | null;
  label?: string;
  note?: string | null;
}

export class ActivityLevel implements IActivityLevel {
  constructor(public id?: string, public order?: number | null, public label?: string, public note?: string | null) {}
}

export function getActivityLevelIdentifier(activityLevel: IActivityLevel): string | undefined {
  return activityLevel.id;
}
