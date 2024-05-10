# -*- coding: utf-8 -*-
"""
Python 3.7.4 (default, Aug  9 2019, 18:34:13) [MSC v.1915 64 bit (AMD64)]
Método de Christian para la sincronización de relojes
Implementación a través de ZeroMQ
"""

import zmq
import datetime
import time
import random
context = zmq.Context()


print("Conectandose al proceso S…")
socket = context.socket(zmq.REQ)
socket.connect("tcp://localhost:4595")
print("Solicitando tiempo a S")

msg = "solicitud de tiempo"
socket.send(msg.encode('utf-8'))
RTT=time.time() #inicio del cronómetro
tiempoP=datetime.datetime.now()#Hora de P

"""
Restando segundos aleatorios para simular una desincronización
"""

tiempoP=tiempoP-datetime.timedelta(seconds=random.randint(0,9))
print("La hora actual en P previo a la sincronización es:\n",tiempoP.strftime("%H:%M:%S"))


"""
Recibiendo respuesta del servidor S
"""

tiempoServidor = socket.recv().decode('utf-8') #Recibiendo tiempo

"""
Una vez recibido la respuesta de S, podemos calcular el Round Trip Time
"""

RTT=time.time()-RTT #fin del cronómetro calculando el Round Trip Time
print("El tiempo que entre envío de solicitud y recepción de la hora (RTT) fue de:\n\tRTT: ",RTT)
print("Recibiendo tiempo de servidor ",tiempoServidor)


"""
Convirtiendo el mensaje decodificado a su respectivo formato datetime para su manipulación
"""
tiempoS=datetime.datetime.strptime(tiempoServidor, "%H:%M:%S")

"""
Calculando el nuevo tiempo del proceso P
"""
tiempoP=tiempoS-(datetime.timedelta(seconds=(int(RTT/2))))
print ("Nueva hora del proceso P\n\t", tiempoP.strftime("%H:%M:%S"))



