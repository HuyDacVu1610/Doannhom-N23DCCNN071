from Company import *

emp_list = [
    SalariedEmp(101, "Nguyen Van A", "0901", "IT", "Manager", 5000, 1000),
    ContractEmp(102, "Tran Thi B", "0902", "HR", "Recruiter", 120, 25),
    SalariedEmp(103, "Le Van C", "0903", "Sales", "Staff", 2000, 300)
]

def safe_input_int(prompt):
    while True:
        try:
            return int(input(prompt))
        except ValueError:
            print("Loi: Vui long nhap mot so nguyen hop le.")

def safe_input_float(prompt):
    while True:
        try:
            return float(input(prompt))
        except ValueError:
            print("Loi: Vui long nhap mot so thuc hop le (vi du: 5000.0).")

def searchById(eid):
    for idx, emp in enumerate(emp_list):
        if emp.get_eid() == eid:
            return idx, emp
    return -1, None

def addEmployee():
    eid = safe_input_int("Nhap Ma NV (EID): ")
    
    pos, _ = searchById(eid)
    if pos != -1:
        print(f"Loi: Ma NV {eid} da ton tai. Them moi that bai.")
        return

    name = input("Nhap ho ten: ")
    mob = input("Nhap SDT: ")
    dept = input("Nhap phong ban: ")
    desg = input("Nhap chuc danh: ")

    print("Ban muon them loai nhan vien nao?")
    print(" 1. Nhan vien chinh thuc (Salaried)")
    print(" 2. Nhan vien hop dong (Contract)")
    type_choice = input("Lua chon (1 hoac 2): ")

    if type_choice == '1':
        sal = safe_input_float("Nhap luong co ban: ")
        bonus = safe_input_float("Nhap bonus: ")
        new_emp = SalariedEmp(eid, name, mob, dept, desg, sal, bonus)
    elif type_choice == '2':
        hrs = safe_input_int("Nhap tong so gio: ")
        charges = safe_input_float("Nhap don gia/gio: ")
        new_emp = ContractEmp(eid, name, mob, dept, desg, hrs, charges)
    else:
        print("Lua chon khong hop le. Huy bo them moi.")
        return
    
    emp_list.append(new_emp)
    print(f"Da them thanh cong nhan vien: {name}")

def displayAll():
    if not emp_list:
        print("--- Chua co nhan vien nao trong danh sach ---")
        return
    
    print("--- DANH SACH TAT CA NHAN VIEN ---")
    for emp in emp_list:
        print(emp)
        print("-" * 30) 

def displayAndCalcByID():
    eid = safe_input_int("Nhap Ma NV can tim: ")
    pos, emp = searchById(eid)
    
    if pos != -1:
        print("--- Tim thay nhan vien ---")
        print(emp) 
        info = emp.companyInfo() 
        print(f"\n\t[Thong tin/Quy dinh]: {info}")
    else:
        print(f"--- Khong tim thay nhan vien voi Ma NV: {eid} ---")

def deleteById():
    eid = safe_input_int("Nhap Ma NV can xoa: ")
    idx, obst = searchById(eid)
    
    if idx != -1:
        print("Ban co chac chan muon xoa nhan vien nay?")
        print(obst)
        ans = input(f"Xac nhan xoa {obst.get_ename()} (y/n)? ")
        
        if ans.lower() == "y":
            emp_list.pop(idx)
            return 1 
        else:
            return 2 
    else:
        return 3 

def updateByID():
    eid = safe_input_int("Nhap Ma NV can cap nhat: ")
    pos, obst = searchById(eid)
    
    if pos != -1:
        print("--- Thong tin hien tai ---")
        print(obst)
        ans = input(f"Ban co muon cap nhat thong tin cho {obst.get_name()} (y/n)? ")
        if ans.lower() != 'y':
            return 2 

        print(f"Phong ban hien tai: {obst.get_dept()}. Nhan Enter de bo qua.")
        dept_new = input("Nhap phong ban moi: ")
        if dept_new: obst.set_dept(dept_new)

        print(f"Chuc danh hien tai: {obst.get_desg()}. Nhan Enter de bo qua.")
        desg_new = input("Nhap chuc danh moi: ")
        if desg_new: obst.set_desg(desg_new)

        if isinstance(obst, SalariedEmp):
            print("--- Cap nhat luong cho NV Chinh thuc ---")
            sal_new = safe_input_float(f"Nhap luong CB moi (hien tai {obst.get_sal():.2f}): ")
            bonus_new = safe_input_float(f"Nhap bonus moi (hien tai {obst.get_bonus():.2f}): ")
            obst.set_sal(sal_new)
            obst.set_bonus(bonus_new)
        
        elif isinstance(obst, ContractEmp):
            print("--- Cap nhat luong cho NV Hop dong ---")
            hrs_new = safe_input_int(f"Nhap so gio moi (hien tai {obst.get_hrs()}): ")
            charges_new = safe_input_float(f"Nhap don gia moi (hien tai {obst.get_charges():.2f}): ")
            obst.set_hrs(hrs_new)
            obst.set_charges(charges_new)
        
        print("--- Da cap nhat thong tin ---")
        print(obst)
        return 1 
    else:
        return 3 

choice = 0
while choice != 6:
    print("\n" + "="*30)
    print("   CHUONG TRINH QUAN LY NHAN VIEN")
    print("="*30)
    print(" 1. Them nhan vien moi")
    print(" 2. Hien thi tat ca nhan vien")
    print(" 3. Tim kiem, tinh luong & xem quy dinh")
    print(" 4. Xoa nhan vien theo Ma NV")
    print(" 5. Cap nhat thong tin nhan vien")
    print(" 6. Thoat")
    print("="*30)
    
    choice = safe_input_int("Vui long nhap lua chon (1-6): ")
    
    match choice:
        case 1:
            print("\n--- Chuc nang 1: Them moi ---")
            addEmployee()
        case 2:
            print("\n--- Chuc nang 2: Hien thi ---")
            displayAll()
        case 3:
            print("\n--- Chuc nang 3: Tim kiem ---")
            displayAndCalcByID()
        case 4:
            print("\n--- Chuc nang 4: Xoa ---")
            status = deleteById()
            if status == 1:
                print("Delete successfully")
            elif status == 2:
                print("Found but not deleted (user cancelled)")
            else:
                print("Not found")
        case 5:
            print("\n--- Chuc nang 5: Cap nhat ---")
            status = updateByID()
            if status == 1:
                print("Modify successfully")
            elif status == 2:
                print("Found but not modified (user cancelled)")
            else:
                print("Not found")
        case 6:
            print("--- Cam on ban da su dung! ---")
        case _:
            print("--- Lua chon sai. Vui long chon tu 1 den 6 ---")