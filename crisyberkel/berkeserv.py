import time

#------------------------------------- Cliente -----------------------------------#
def Cliente():
    pass

def getHoraCliente1():
    horaUTC = time.localtime() 
    desface = 20
    minutos  = int(horaUTC[3])*60 + int(horaUTC[4])+ desface 
    horaTotal = [int(minutos/60), minutos%60] 
    return horaTotal 

def setHoraCliente1():
    pass

def getHoraCliente2():
    horaUTC = time.localtime() 
    desface = -10
    minutos  = int(horaUTC[3])*60 + int(horaUTC[4])+ desface 
    horaTotal = [int(minutos/60), minutos%60]
    return horaTotal 

def setHoraCliente2():
    pass

#------------------------------------- Servidor ----------------------------------#
def getHoraServer():
    horaUTC = time.localtime() 
    horaServer = [horaUTC[3], horaUTC[4]] 
    return horaServer 

def setHoraServer():
    pass

def calcularDiferencias(horaCliente, horaServer):
    minutosCliente = int(horaCliente[0])*60  + int(horaCliente[1]) 
    minutosServer = int(horaServer[0])*60 + int(horaServer[1]) 
    diferencia = minutosCliente - minutosServer 
    return str(diferencia)

def calcularHoras(diferencias, *horas):
    print("horas: "+str(horas))
    minutos = []
    i = 0
    for hora in horas:
        while(i<len(horas)):
            minutos.append(int(horas[i][0])*60+int(horas[i][1]))
            i += 1
    print("minutos: "+ str(minutos))
    cantidadDiferencias = len(diferencias)
    suma = 0
    for diferencia in diferencias:
        suma = suma + int(diferencia)
    promedio = suma / cantidadDiferencias
    print("promedio : " + str(promedio))
    nuevasDiferencias = []
    for diferencia in diferencias:
        nuevasDiferencias.append(promedio-int(diferencia))
    print("nuevas Diferencias: "+ str(nuevasDiferencias))
    pos = 0
    nuevasHoras = []
    for nuevaDiferencia in nuevasDiferencias:
        nuevasHoras.append(minutos[pos] + int(nuevaDiferencia))
        pos += 1
    print("nuevas Horas: "+ str(nuevasHoras))
    return nuevasHoras

def obtenerHoraCliente():
    horas = []
    diferencias = [] 
    horaServer = getHoraServer() 
    print("hora servidor : "+ str(horaServer))
    diferencias.append(calcularDiferencias(horaServer, horaServer))
    horas.append(str(horaServer))
    horaCliente1 = getHoraCliente1() 
    print("hora cliente 1: "+ str(horaCliente1))
    diferencias.append(calcularDiferencias(horaCliente1, horaServer))
    horas.append(str(horaCliente1))
    horaCliente2 = getHoraCliente2() 
    print("hora cliente 2: "+ str(horaCliente2))
    diferencias.append(calcularDiferencias(horaCliente2, horaServer))
    horas.append(str(horaCliente2))
    strHoras = ' '.join(horas) 
    bracketLeft = strHoras.replace("[", "")
    bracketRight = bracketLeft.replace ("]", "")
    commas = bracketRight.replace (",", "")
    print(commas)
    hrs = commas.split(" ")
    print(hrs)
    i = 0
    horas = []
    while i < len(hrs):
        hourAux = []
        hourAux.append(hrs[i])
        hourAux.append(hrs[i+1])
        horas.append(hourAux)
        i+=2
    print("diferencias: "+str(diferencias))
    nuevasHoras = calcularHoras(diferencias, *horas)
    print(nuevasHoras)
    nuevaHora = []

obtenerHoraCliente()
