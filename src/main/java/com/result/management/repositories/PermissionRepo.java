package com.result.management.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.result.management.entities.Permission;
import com.result.management.entities.Subject;
import com.result.management.entities.Teacher;

@Repository
public interface PermissionRepo extends CrudRepository<Permission, Integer> {

    public Permission findPermissionByclass1AndSubjectAndAssigned(com.result.management.entities.Class class1,
			Subject subject,boolean b);

	public List<Permission> findByTeacherAndAssigned(Teacher teacher, boolean b);

	public List<Permission> findPermissionByclass1IdAndAssigned(int classId, boolean b);

	public List<Permission> findPermissionByClass1AndTeacherAndAssigned(com.result.management.entities.Class class1, Teacher t, boolean b);

	public List<Permission> findPermissionByclass1IdAndTeacherIdAndAssigned(int cId, int teacherId, boolean b);

	public void deleteBySubjectAndAssigned(Subject subject, boolean b);

	public List<Permission> findBySubjectIdAndAssigned(int sId, boolean b);

	public Permission findByClass1IdAndSubjectIdAndAssigned(int cId, int subId, boolean b);

	public List<Permission> findByTeacherIdAndAssigned(int teacherId, boolean b);

	public List<Permission> findPermissionByclass1Id(int cId);

	public List<Permission> findPermissionBySubject(Subject subject);

	public Permission findPermissionByclass1AndSubject(com.result.management.entities.Class cl, Subject sub);

	public List<Permission> findBySubjectId(int sId);

	public List<Permission> findByTeacher(Teacher teacher);
}
