package com.atm.chatonline.bbs.commom;
/**
 * 用于存储系别，学校，专业的集合
 * 2015.7.24，atm--李
 */
import com.example.studentsystem01.R;

public class RegisterSet {
		//各个学校系别集合
		final int[] department={
				R.array.jr_school_dept,R.array.gy_school_dept
		};
		//金融学院各系的专业集合
		final int[] countofJr={R.array.jr_it_major,R.array.jr_ec_major
				};
		//工业大学各系的专业集合
		final int[] countofGy={R.array.gy_gc_major,R.array.gy_jd_major};
		public int[] getDepartment() {
			return department;
		}
		public int[] getCountofjr() {
			return countofJr;
		}
		public int[] getCountofgy() {
			return countofGy;
		}
		
}
