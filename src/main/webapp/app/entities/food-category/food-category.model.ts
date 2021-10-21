import { IPatient } from 'app/entities/patient/patient.model';

export interface IFoodCategory {
  id?: string;
  name?: string | null;
  description?: string | null;
  foodCategories?: IPatient[] | null;
}

export class FoodCategory implements IFoodCategory {
  constructor(
    public id?: string,
    public name?: string | null,
    public description?: string | null,
    public foodCategories?: IPatient[] | null
  ) {}
}

export function getFoodCategoryIdentifier(foodCategory: IFoodCategory): string | undefined {
  return foodCategory.id;
}
