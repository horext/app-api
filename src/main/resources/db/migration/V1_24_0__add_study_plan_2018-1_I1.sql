insert into study_plan (id, code, from_date, name, to_date, organization_unit_id)
values (3, '2018-1_I1', '2021-03-22 18:42:43.146000', null, null, 11);

insert into public.subject (id, credits, cycle, max_cycle, min_cycle, note, required_credits, total_weekly_hours,
                            weekly_laboratory_hours, weekly_practice_hours, weekly_practice_laboratory_hours,
                            weekly_theory_hours, course_id, evaluation_system_id, study_plan_id, subject_type_id, order)
values (3310, 2, 1, null, null, null, null, 3, null, null, 2, 1, 'BEF01', 5, 3, 1, null),
       (3307, 5, 1, null, null, null, null, 6, null, null, 2, 4, 'BMA01', 6, 3, 1, null),
       (3308, 5, 1, null, null, null, null, 6, null, null, 2, 4, 'BQU01', 7, 3, 1, null),
       (3309, 2, 1, null, null, null, null, 3, null, null, 2, 1, 'BRC01', 5, 3, 1, null),
       (3311, 3, 1, null, null, null, null, 4, null, null, 2, 2, 'FB101', 6, 3, 1, null),
       (3312, 2, 1, null, null, null, null, 4, null, null, 4, 0, 'TE101', 5, 3, 1, null),
       (3313, 3, 1, null, null, null, null, 4, null, null, 2, 2, 'GE101', 7, 3, 1, null),
       (3317, 3, 2, null, null, null, null, 4, null, null, 2, 2, 'BRN01', 5, 3, 1, null),
       (3319, 2, 2, null, null, null, null, 4, null, null, 4, 0, 'SI204', 5, 3, 1, null),
       (3318, 2, 2, null, null, null, null, 4, null, null, 4, 0, 'HU102', 5, 3, 1, null),
       (3314, 4, 2, null, null, null, null, 5, null, null, 2, 3, 'BMA03', 6, 3, 1, null),
       (3315, 5, 2, null, null, null, null, 6, null, null, 2, 4, 'BMA02', 6, 3, 1, null),
       (3320, 4, 2, null, null, null, null, 5, null, null, 2, 3, 'FB202', 7, 3, 1, null),
       (3316, 2, 2, null, null, null, null, 3, null, null, 2, 1, 'BIC01', 7, 3, 1, null),
       (3325, 3, 3, null, null, null, null, 4, null, null, 2, 2, 'TE302', 5, 3, 1, null),
       (3324, 5, 3, null, null, null, null, 6, null, null, 2, 4, 'FB303', 6, 3, 1, null),
       (3323, 3, 3, null, null, null, null, 4, null, null, 2, 2, 'FB301', 6, 3, 1, null),
       (3322, 2, 3, null, null, null, null, 4, null, null, 4, 0, 'HU301', 5, 3, 1, null),
       (3321, 5, 3, null, null, null, null, 6, null, null, 2, 4, 'BFI01', 7, 3, 1, null),
       (3326, 4, 3, null, null, null, null, 5, null, null, 2, 3, 'TE301', 7, 3, 1, null),
       (3329, 5, 4, null, null, null, null, 6, null, null, 2, 4, 'FB401', 6, 3, 1, null),
       (3330, 3, 4, null, null, null, null, 5, null, null, 4, 1, 'SI401', 7, 3, 1, null),
       (3331, 3, 4, null, null, null, null, 4, null, null, 2, 2, 'FB305', 6, 3, 1, null),
       (3327, 3, 4, null, null, null, null, 4, null, null, 2, 2, 'BEG01', 7, 3, 1, null),
       (3328, 5, 4, null, null, null, null, 6, null, null, 2, 4, 'FB403', 6, 3, 1, null),
       (3332, 3, 4, null, null, null, null, 4, null, null, 2, 2, 'TE401', 7, 3, 1, null),
       (3334, 3, 5, null, null, null, null, 4, null, null, 2, 2, 'FB405', 6, 3, 1, null),
       (3333, 2, 5, null, null, null, null, 2, null, null, 0, 2, 'HU201', 2, 3, 1, null),
       (3306, 3, 5, null, null, null, null, 4, null, null, 2, 2, 'TE603', 6, 3, 1, null),
       (3339, 3, 5, null, null, null, null, 4, null, null, 2, 2, 'SI501', 7, 3, 1, null),
       (3338, 3, 5, null, null, null, null, 4, null, null, 2, 2, 'GE502', 7, 3, 1, null),
       (3337, 4, 5, null, null, null, null, 5, null, null, 2, 3, 'TE501', 7, 3, 1, null),
       (3336, 4, 5, null, null, null, null, 6, null, null, 4, 2, 'TE503', 7, 3, 1, null),
       (3335, 3, 5, null, null, null, null, 4, null, null, 2, 2, 'TE502', 7, 3, 1, null),
       (3340, 3, 6, null, null, null, null, 4, null, null, 2, 2, 'GE604', 7, 3, 1, null),
       (3341, 4, 6, null, null, null, null, 6, null, null, 4, 2, 'TE602', 7, 3, 1, null),
       (3342, 3, 6, null, null, null, null, 4, null, null, 2, 2, 'GE602', 7, 3, 1, null),
       (3343, 3, 6, null, null, null, null, 4, null, null, 2, 2, 'GE603', 7, 3, 1, null),
       (3344, 3, 6, null, null, null, null, 4, null, null, 2, 2, 'SI601', 7, 3, 1, null),
       (3345, 3, 6, null, null, null, null, 4, null, null, 2, 2, 'TE601', 7, 3, 1, null),
       (3350, 3, 7, null, null, null, null, 4, null, null, 2, 2, 'SI503', 7, 3, 1, null),
       (3351, 3, 7, null, null, null, null, 4, null, null, 2, 2, 'GE704', 7, 3, 1, null),
       (3349, 3, 7, null, null, null, null, 4, null, null, 2, 2, 'TE701', 7, 3, 1, null),
       (3348, 3, 7, null, null, null, null, 4, null, null, 2, 2, 'SI701', 7, 3, 1, null),
       (3347, 3, 7, null, null, null, null, 4, null, null, 2, 2, 'GE701', 7, 3, 1, null),
       (3346, 3, 7, null, null, null, null, 4, null, null, 2, 2, 'GE702', 7, 3, 1, null),
       (3356, 3, 8, null, null, null, null, 4, null, null, 2, 2, 'GE805', 7, 3, 1, null),
       (3352, 3, 8, null, null, null, null, 4, null, null, 2, 2, 'GE709', 7, 3, 1, null),
       (3353, 3, 8, null, null, null, null, 4, null, null, 2, 2, 'TE801', 7, 3, 1, null),
       (3354, 3, 8, null, null, null, null, 4, null, null, 2, 2, 'GE802', 7, 3, 1, null),
       (3355, 3, 8, null, null, null, null, 4, null, null, 2, 2, 'TE802', 7, 3, 1, null),
       (3360, 2, 9, null, null, null, null, 4, null, null, 4, 0, 'GE904', 5, 3, 1, null),
       (3357, 3, 9, null, null, null, null, 4, null, null, 2, 2, 'TE901', 7, 3, 1, null),
       (3358, 3, 9, null, null, null, null, 4, null, null, 2, 2, 'GE002', 7, 3, 1, null),
       (3359, 3, 9, null, null, null, null, 4, null, null, 2, 2, 'GE901', 7, 3, 1, null),
       (3361, 3, 9, null, null, null, null, 4, null, null, 2, 2, 'GE905', 7, 3, 1, null),
       (3365, 2, 10, null, null, null, null, 4, null, null, 4, 0, 'GE001', 5, 3, 1, null),
       (3362, 3, 10, null, null, null, null, 4, null, null, 2, 2, 'GE903', 7, 3, 1, null),
       (3363, 3, 10, null, null, null, null, 4, null, null, 2, 2, 'GE801', 7, 3, 1, null),
       (3364, 3, 10, null, null, null, null, 4, null, null, 2, 2, 'GE902', 7, 3, 1, null),
       (3388, 3, null, null, null, null, null, 4, null, null, 2, 2, 'TE123', 7, 3, 2, null),
       (3371, 3, null, null, null, null, null, 4, null, null, 2, 2, 'SI112', 7, 3, 3, null),
       (3372, 3, null, null, null, null, null, 4, null, null, 2, 2, 'SI113', 7, 3, 3, null),
       (3373, 3, null, null, null, null, null, 4, null, null, 2, 2, 'SI114', 7, 3, 3, null),
       (3374, 2, null, null, null, null, null, 4, null, null, 0, 0, 'SI115', 5, 3, 3, null),
       (3375, 2, null, null, null, null, null, 4, null, null, 4, 0, 'GE116', 7, 3, 3, null),
       (3376, 2, null, null, null, null, null, 4, null, null, 4, 0, 'GE113', 5, 3, 3, null),
       (3377, 3, null, null, null, null, null, 4, null, null, 2, 2, 'GE114', 7, 3, 3, null),
       (3378, 2, null, null, null, null, null, 2, null, null, 0, 2, 'GE115', 5, 3, 3, null),
       (3379, 3, null, null, null, null, null, 4, null, null, 2, 2, 'SI120', 7, 3, 3, null),
       (3380, 2, null, null, null, null, null, 2, null, null, 0, 2, 'HU115', 5, 3, 3, null),
       (3381, 3, null, null, null, null, null, 4, null, null, 2, 2, 'GE121', 7, 3, 2, null),
       (3382, 3, null, null, null, null, null, 4, null, null, 2, 2, 'GE122', 7, 3, 2, null),
       (3383, 3, null, null, null, null, null, 4, null, null, 2, 2, 'TE125', 7, 3, 2, null),
       (3384, 3, null, null, null, null, null, 4, null, null, 2, 2, 'GE123', 7, 3, 2, null),
       (3385, 3, null, null, null, null, null, 4, null, null, 2, 2, 'GE124', 7, 3, 2, null),
       (3386, 3, null, null, null, null, null, 4, null, null, 2, 2, 'TE121', 7, 3, 2, null),
       (3387, 3, null, null, null, null, null, 4, null, null, 2, 2, 'TE122', 7, 3, 2, null),
       (3389, 3, null, null, null, null, null, 4, null, null, 2, 2, 'SI124', 7, 3, 2, null),
       (3390, 3, null, null, null, null, null, 4, null, null, 2, 2, 'TE124', 7, 3, 2, null),
       (3391, 3, null, null, null, null, null, 4, null, null, 2, 2, 'TE127', 7, 3, 2, null),
       (3392, 3, null, null, null, null, null, 4, null, null, 2, 2, 'SI125', 7, 3, 2, null),
       (3393, 3, null, null, null, null, null, 4, null, null, 2, 2, 'GE126', 7, 3, 2, null),
       (3394, 3, null, null, null, null, null, 4, null, null, 2, 2, 'GE128', 7, 3, 2, null),
       (3395, 3, null, null, null, null, null, 4, null, null, 2, 2, 'TE126', 7, 3, 2, null),
       (3370, 2, null, null, null, null, null, 3, null, null, 2, 1, 'GE117', 5, 3, 3, null),
       (3369, 2, null, null, null, null, null, 3, null, null, 2, 1, 'HU112', 5, 3, 3, null),
       (3368, 2, null, null, null, null, null, 2, null, null, 0, 2, 'HU111', 5, 3, 3, null),
       (3367, 3, null, null, null, null, null, 4, null, null, 2, 2, 'TE111', 7, 3, 3, null),
       (3366, 3, null, null, null, null, null, 4, null, null, 2, 2, 'SI111', 7, 3, 3, null);