// See LICENSE for license details.

package example

import chisel3._

class apb_bus (bus_w: UInt) extends Bundle{
  val paddr   = Input(UInt(bus_w.W))
  val pwrite  = Input(Bool())
  val psel    = Input(Bool())
  val penable = Input(Bool())
  val pwdata  = Input(UInt(bus_w.W))
  val pready  = Output(Bool())
}

class conf_port (port_num, port_w: UInt) extends Bundle{
  val we_vec    = Vec(port_num, Input(Bool()))
  val port_vec  = Vec(port_num, Output(UInt(port_w.W)))
}

class config_if [per_bus <: peripheral_bus]
(bus_w, port_num: UInt, bus: per_bus)
extends Bundle{
  val conf_port = new conf_port(port_num, bus_w)
  val bus       = new bus(bus_w)
}

class apb_config_controller (bus_w, port_num: UInt)
extends Module {
  val io = new config_if(abp_bus, bus_w, port_num)
  // config controller goes here
}

class wrapper extends Module {
  val io = IO(new Bundle {
    val bus   = new apb_bus(pbus_w)
    val ssbus = new axis_bus(sbus_w)
    val smbus = new axis_bus(sbus_w).flip
  })

  val x  = Reg(UInt())
  val y  = Reg(UInt())

  when (x > y) { x := x - y }
    .otherwise { y := y - x }

  when (io.e) { x := io.a; y := io.b }
  io.z := x
  io.v := y === 0.U
}
