#/ debug display result
[keyword]Подключить связанные классы=import ru.ulpfr.pension_brms.gui.MainWindow;import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;import ru.ulpfr.pension_brms.model.*;import ru.ulpfr.pension_brms.model.Client.GENDER;import ru.ulpfr.pension_brms.model.Client.NATIONALITIES;import ru.ulpfr.pension_brms.model.Pension.PENS_STATUS;import ru.ulpfr.pension_brms.model.Pension.PENS_TYPE;import ru.ulpfr.pension_brms.model.Right.RIGHT_TYPES;import ru.ulpfr.pension_brms.model.RightError.ERROR_TYPES;
[keyword]Отключить цикличность=no-loop true

[condition]and=&&
[condition]or=||
[condition]менее чем или равно=<=
[condition]менее чем=<
[condition]более чем или равно=>=
[condition]более чем=>
[condition]идентично===

[condition]Клиент=$c:Client($id:id)
[condition]- является гражданином РФ=nationality == NATIONALITIES.RUSSIA
[condition]- не является гражданином РФ=nationality != NATIONALITIES.RUSSIA
[condition]- является женщиной=gender == GENDER.FEMALE
[condition]- является мужчиной=gender == GENDER.MALE
[condition]- стаж работы {operator} "{const_stag}"=workExperience {operator} Constants.getConstantNumValue("{const_stag}")
[condition]- возраст {operator} "{const_age}"=age {operator} Constants.getConstantNumValue("{const_age}")
[condition]- Размер ИПК {operator} "{const_ipk}"=IPK {operator} Constants.getConstantFloatValue("{const_ipk}")

[condition]Имеет запрос на пенсию=$p: Pension( clientId  == $id)
[condition]Имеет ошибку права на страховую пенсию по старости=$e: RightError( clientId == $id, errorCode == ERROR_TYPES.PENS_AGE)
[condition]- вид пенсии: страховая по старости=pensType == PENS_TYPE.Old_age
[condition]- статус пенсии: не определен=status == PENS_STATUS.NA
[condition]- статус пенсии: назначена=status == PENS_STATUS.ASSIGNED
[condition]- статус пенсии: отклонена=status == PENS_STATUS.REJECTED

[condition]В наличии право для данного вида пенсии=Right(clientId  == $id, pensType == $p.getPensType())
[condition]Отсутствует право для данного вида пенсии=not Right(clientId  == $id, pensType == $p.getPensType())
[condition]- по критерию: гражданство=type == RIGHT_TYPES.NATIONALITY
[condition]- по критерию: возраст=type == RIGHT_TYPES.AGE
[condition]- по критерию: минимальный стаж=type == RIGHT_TYPES.MIN_EXP
[condition]- по критерию: минимальный ИПК=type == RIGHT_TYPES.MIN_IPK



[consequence]Вывести в консоль: "{msg}"=System.out.println("{msg}");
[consequence]Добавить право на пенсию типа {ptype} по критерию: {rtype}=insert(new Right($c.getId(), {rtype}, {ptype}));
[consequence]Добавить право на страховую пенсию по старости по критерию: национальнось=insert(new Right($c.getId(), RIGHT_TYPES.NATIONALITY, PENS_TYPE.Old_age));
[consequence]Добавить право на страховую пенсию по старости по критерию: возраст=insert(new Right($c.getId(), RIGHT_TYPES.AGE, PENS_TYPE.Old_age));
[consequence]Добавить право на страховую пенсию по старости по критерию: минимальный стаж=insert(new Right($c.getId(), RIGHT_TYPES.MIN_EXP));
[consequence]Добавить право на страховую пенсию по старости по критерию: минимальный ИПК=insert(new Right($c.getId(), RIGHT_TYPES.MIN_IPK));
[consequence]Обновить показатели пенсии:=modify($p)\{\}
[consequence]- статус пенсии: назначена=setStatus(PENS_STATUS.ASSIGNED)
[consequence]- статус пенсии: отклонена=setStatus(PENS_STATUS.REJECTED)
[consequence]- размер пенсии рассчитать по формуле: ИПК {operator} СИПК=setAmount($c.getIPK() {operator} $c.getIPKcost())
[consequence]- размер пенсии: {num}=setAmount({num})
[consequence]Добавить ошибку права на страховую пенсию по старости с формулировкой "{const_err}"=insert(new RightError(Constants.getConstantValue("{const_err}"),ERROR_TYPES.PENS_AGE, $c.getId())); 

//Выxодные сообщения
[consequence]Вывод: {message}=MainWindow.getInstance().output({message});
[consequence]сообщение о причине отказа="Отказ для клиента #"+$id+": "+$e.getErrorMessage()
[consequence]сообщение о назначенной пенсии="Клиенту #"+$id+" положена страxовая пенсия по старости."+System.lineSeparator()+"Размер пенсии равен = " + $p.getAmount()+ " руб."







