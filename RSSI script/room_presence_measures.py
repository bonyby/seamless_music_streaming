import asyncio
from urllib.request import urlopen
import websockets
import json
import PySimpleGUI as sg
from threading import Thread

url = "ws://192.168.1.227:6415"
subscribeJson = {
  "event": "subscribeEvents",
  "data": {
    "type": "entityUpdates"
  }
}

entities = dict() # ENTITY: id:{name, lastUpdatedAt, distances}
log = dict() # Log entry: name:{beacon, count}
prevNumbOfEntities = 0

layout =[[sg.Button("SAVE"), sg.Button("EXIT")]]

window = sg.Window(title="Room Assistant Measurements")
exited = False

def main():
    setup()
    thread = Thread(target=runConnAsync)
    thread.start()
    windowEvents()

def setup():
    global window
    window.Layout(layout)
    window.read(timeout=100)

def runConnAsync():
    asyncio.run(conn())

def windowEvents():
    while True:
        event, _ = window.read(timeout=100)

        if event == "SAVE":
            save()
        elif event == "EXIT":
            save()
            global exited
            exited = True
            break

        updateWindow()
    
    window.close()
    quit()
        
def save():
    with open("result.txt", "w") as file:
        for entry in log:
            file.write("{0}:\n".format(entry))
            for dict in log[entry]:
                file.write("    {0}: {1}\n".format(dict, log[entry][dict]))

            file.write("\n")

def updateWindow():
    global window
    global layout
    global prevNumbOfEntities

    # Restart window with new layout if a new entity has been registered
    if prevNumbOfEntities < len(entities):
        window.close()
    
        layout = [[sg.Button("SAVE"), sg.Button("EXIT")]]
        for id in entities:
            entity = entities[id]
            layout.append([sg.Text("{0} ({1}):".format(entity['name'], id))])
            layout.append([sg.Text("Last updated at: {0}".format(entity.get('lastUpdatedAt', "no entry")), key="{0}:upd".format(id))])
            layout.append([sg.Text("Distances:")])
            distances = entity.get("distances")
            if distances is not None:
                for entry in distances:
                    layout.append([sg.Text("    {0}: {1}".format(entry, distances[entry].get("distance")), key="{0}:{1}:dist".format(id, entry))])
            layout.append([sg.Text("")])

        window = sg.Window(title="Room Assistant Measurements")
        window.Layout(layout)
        window.read(timeout=0)
        prevNumbOfEntities = len(entities)
    # Else just update the values
    else:
        for id in entities:
            entity = entities[id]
            str = "{0}:upd".format(id)
            if not str in window.AllKeysDict:
                continue
            window[str].update("Last updated at: {0}".format(entity.get('lastUpdatedAt', "no entry")))
            distances = entity.get("distances")
            if distances is not None:
                for entry in distances:
                    str = "{0}:{1}:dist".format(id, entry)
                    if str in window.AllKeysDict:
                        window[str].update("    {0}: {1}".format(entry, distances[entry].get("distance")))
    
        window.read(timeout=0)

async def conn():
    async with websockets.connect(url) as websocket:
        await websocket.send(json.dumps(subscribeJson))
        await websocket.recv()        

        while True:
            global exited
            if exited:
                break
            try:
                msg = await websocket.recv()
            except websockets.ConnectionClosedOK:
                break
            
            entity = json.loads(msg)['entity']
            # print("entity: " + str(entity))
            id = entity['id']

            # ignore everything that is not a room presence
            name = entity['name']
            if not "Room Presence" in name:
                continue

            distances = entity.get('distances')

            # update entities dictionary
            entities[id] = dict()
            entities[id]['name'] = name
            attributes = entity.get('attributes')
            if attributes is not None:
                lastUpd = attributes.get('lastUpdatedAt')
                if lastUpd is not None:
                    entities[id]['lastUpdatedAt'] = lastUpd
            entities[id]['distances'] = distances

            # update log dictionary
            if not name in log:
                log[name] = dict()

            countDict = log[name]
            closestBeacon = getClosestBeacon(distances)

            if not closestBeacon in countDict:
                countDict[closestBeacon] = 0
            
            countDict[closestBeacon] += 1
            
def getClosestBeacon(distances):
    closest = ""
    dist = 10000000 # arbitrary high number
    for entry in distances:
        entryDist = distances[entry]["distance"]
        if entryDist < dist:
            closest = entry
            dist = entryDist

    return closest

if __name__ == "__main__":
    main()