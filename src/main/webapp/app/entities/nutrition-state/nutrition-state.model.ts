export interface INutritionState {
  id?: string;
  order?: number | null;
  label?: string;
  note?: string | null;
}

export class NutritionState implements INutritionState {
  constructor(public id?: string, public order?: number | null, public label?: string, public note?: string | null) {}
}

export function getNutritionStateIdentifier(nutritionState: INutritionState): string | undefined {
  return nutritionState.id;
}
