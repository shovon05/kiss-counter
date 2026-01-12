val engine = SimpleKissEngine()

engine.setOnKissDetectedListener {
    println("Kiss detected! Count=${engine.getKissCount()}")
}

engine.start()

engine.simulateKiss(0.92f)
engine.simulateKiss(0.88f)

engine.stop()
