import * as dayjs from 'dayjs';
import { INutritionState } from 'app/entities/nutrition-state/nutrition-state.model';
import { IActivityLevel } from 'app/entities/activity-level/activity-level.model';
import { IDietNature } from 'app/entities/diet-nature/diet-nature.model';
import { ISupplements } from 'app/entities/supplements/supplements.model';
import { IFoodCategory } from 'app/entities/food-category/food-category.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { WeightType } from 'app/entities/enumerations/weight-type.model';
import { HeightMeasureType } from 'app/entities/enumerations/height-measure-type.model';
import { GainLossType } from 'app/entities/enumerations/gain-loss-type.model';
import { AppetiteType } from 'app/entities/enumerations/appetite-type.model';
import { PhysicalActivityType } from 'app/entities/enumerations/physical-activity-type.model';
import { ReligionType } from 'app/entities/enumerations/religion-type.model';
import { AreaType } from 'app/entities/enumerations/area-type.model';

export interface IPatient {
  id?: string;
  name?: string | null;
  address?: string | null;
  hospital?: string | null;
  admissionDate?: dayjs.Dayjs | null;
  reasonOfAdmission?: string | null;
  wordNo?: string | null;
  bedNo?: string | null;
  healthCondition?: string | null;
  mentalStatus?: string | null;
  age?: number | null;
  sex?: Gender | null;
  weight?: number | null;
  weightType?: WeightType | null;
  height?: number | null;
  heightMeasureType?: HeightMeasureType | null;
  ibw?: number | null;
  bmi?: number | null;
  recentWeightGainLoss?: boolean | null;
  gainLossMeasure?: number | null;
  gainLossTimeFrame?: number | null;
  gainLossType?: GainLossType | null;
  supplementTaken?: boolean | null;
  appetite?: AppetiteType | null;
  physicalActivity?: PhysicalActivityType | null;
  monthlyFamilyIncome?: number | null;
  religion?: ReligionType | null;
  education?: string | null;
  occupation?: string | null;
  livingStatus?: string | null;
  area?: AreaType | null;
  estimatedEnergyNeeds?: string | null;
  calculateEnergyNeeds?: string | null;
  totalKCal?: number | null;
  carbohydrate?: number | null;
  protein?: number | null;
  fat?: number | null;
  fluid?: number | null;
  foodRestriction?: boolean | null;
  nutritionState?: INutritionState | null;
  activityLevel?: IActivityLevel | null;
  dietNatures?: IDietNature[] | null;
  supplements?: ISupplements[] | null;
  restrictedFoodCategories?: IFoodCategory[] | null;
}

export class Patient implements IPatient {
  constructor(
    public id?: string,
    public name?: string | null,
    public address?: string | null,
    public hospital?: string | null,
    public admissionDate?: dayjs.Dayjs | null,
    public reasonOfAdmission?: string | null,
    public wordNo?: string | null,
    public bedNo?: string | null,
    public healthCondition?: string | null,
    public mentalStatus?: string | null,
    public age?: number | null,
    public sex?: Gender | null,
    public weight?: number | null,
    public weightType?: WeightType | null,
    public height?: number | null,
    public heightMeasureType?: HeightMeasureType | null,
    public ibw?: number | null,
    public bmi?: number | null,
    public recentWeightGainLoss?: boolean | null,
    public gainLossMeasure?: number | null,
    public gainLossTimeFrame?: number | null,
    public gainLossType?: GainLossType | null,
    public supplementTaken?: boolean | null,
    public appetite?: AppetiteType | null,
    public physicalActivity?: PhysicalActivityType | null,
    public monthlyFamilyIncome?: number | null,
    public religion?: ReligionType | null,
    public education?: string | null,
    public occupation?: string | null,
    public livingStatus?: string | null,
    public area?: AreaType | null,
    public estimatedEnergyNeeds?: string | null,
    public calculateEnergyNeeds?: string | null,
    public totalKCal?: number | null,
    public carbohydrate?: number | null,
    public protein?: number | null,
    public fat?: number | null,
    public fluid?: number | null,
    public foodRestriction?: boolean | null,
    public nutritionState?: INutritionState | null,
    public activityLevel?: IActivityLevel | null,
    public dietNatures?: IDietNature[] | null,
    public supplements?: ISupplements[] | null,
    public restrictedFoodCategories?: IFoodCategory[] | null
  ) {
    this.recentWeightGainLoss = this.recentWeightGainLoss ?? false;
    this.supplementTaken = this.supplementTaken ?? false;
    this.foodRestriction = this.foodRestriction ?? false;
  }
}

export function getPatientIdentifier(patient: IPatient): string | undefined {
  return patient.id;
}
