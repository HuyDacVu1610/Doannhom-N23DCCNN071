from abc import ABC, abstractmethod

class Person:
    def __init__(self, eid=0, ename="", mob=""):
        self.__eid = eid
        self.__ename = ename.strip()
        self.__mob = mob

    def set_eid(self, eid):
        self.__eid = eid
    def get_eid(self):
        return self.__eid

    def set_ename(self, ename):
        self.__ename = ename.strip()
    def get_ename(self):
        return self.__ename

    def set_mob(self, mob):
        self.__mob = mob
    def get_mob(self):
        return self.__mob

    def __str__(self):
        return f"ID: {self.__eid}, Ten: {self.__ename}, SDT: {self.__mob}"

class Employee(Person, ABC):
    def __init__(self, eid=0, ename="", mob="", dept="", desg=""):
        super().__init__(eid, ename, mob)
        self.__dept = dept
        self.__desg = desg

    def set_dept(self, dept):
        self.__dept = dept
    def get_dept(self):
        return self.__dept

    def set_desg(self, desg):
        self.__desg = desg
    def get_desg(self):
        return self.__desg

    @abstractmethod
    def calcSal(self):
        pass

    def getnewsofcompany(self):
        return "Company profit grow by 50%"

    def __str__(self):
        person_info = super().__str__()
        return f"{person_info}, Phong: {self.__dept}, Chuc: {self.__desg}"

class SalariedEmp(Employee):
    BONUS_RATE = 0.10
    HRA_RATE = 0.12
    PF_RATE = 0.08

    def __init__(self, eid=0, ename="", mob="", dept="", desg="", sal=0.0, bonus=0.0):
        super().__init__(eid, ename, mob, dept, desg)
        self.__sal = float(sal)
        self.__bonus = float(bonus)

    def set_sal(self, sal):
        self.__sal = float(sal) if sal >= 0 else 0.0
    def get_sal(self):
        return self.__sal

    def set_bonus(self, bonus):
        self.__bonus = float(bonus) if bonus >= 0 else 0.0
    def get_bonus(self):
        return self.__bonus

    def calcSal(self):
        return (self.__sal + 
                (self.BONUS_RATE * self.__sal) + 
                (self.HRA_RATE * self.__sal) - 
                (self.PF_RATE * self.__sal) + 
                self.__bonus)

    def getnewsofcompany(self):
        return "Company profit grow by 50%"

    def __str__(self):
        emp_info = super().__str__()
        net_sal = self.calcSal()
        return (f"{emp_info}\n\t[Loai: Chinh thuc, Luong CB: {self.__sal:.2f}, "
                f"Bonus: {self.__bonus:.2f}, Luong Net: {net_sal:.2f}]")

class ContractEmp(Employee):
    def __init__(self, eid=0, ename="", mob="", dept="", desg="", hrs=0, charges=0.0):
        super().__init__(eid, ename, mob, dept, desg)
        self.set_hrs(hrs)
        self.set_charges(charges)

    def set_hrs(self, hrs):
        try:
            self.__hrs = int(hrs)
            if self.__hrs < 0: self.__hrs = 0
        except ValueError:
            self.__hrs = 0
    def get_hrs(self):
        return self.__hrs

    def set_charges(self, charges):
        try:
            self.__charges = float(charges)
            if self.__charges < 0: self.__charges = 0.0
        except ValueError:
            self.__charges = 0.0
    def get_charges(self):
        return self.__charges

    def calcSal(self):
        return self.__hrs * self.__charges

    def companyInfo(self):
        return "Quy dinh"

    def __str__(self):
        emp_info = super().__str__()
        net_sal = self.calcSal()
        return (f"{emp_info}\n\t[Loai: Hop dong, So gio: {self.__hrs}, "
                f"Don gia: {self.__charges:.2f}, Luong Net: {net_sal:.2f}]")