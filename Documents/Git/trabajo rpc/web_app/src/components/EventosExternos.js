import React, { useEffect, useState } from "react";
import {
    listarEventosExternos,
    listarEventosPropios,
    publicarEvento,
    darBajaEvento,
    notificarAdhesion,
} from "../servicios/eventosCliente";
import "./DonacionesYeventosKf.css";

export default function EventosExternos({ usuarioLogueado }) {

    const [eventos, setEventos] = useState([]);
    const [misEventos, setMisEventos] = useState([]);
    const [nuevoEvento, setNuevoEvento] = useState({
        idOrganizacion: "ONG001",
        idEvento: "",
        nombre: "",
        descripcion: "",
        fechaHora: "",
    });

    // !Carga los eventos externos cada 5 segundos
    useEffect(() => {
        const fetchEventos = async () => {
            try {
                const externos = await listarEventosExternos();
                setEventos(externos);

                const propios = await listarEventosPropios();
                setMisEventos(propios);
            } catch (err) {
                console.error("Error al listar eventos:", err);
            }
        };
        fetchEventos();
        const interval = setInterval(fetchEventos, 5000);
        return () => clearInterval(interval);
    }, []);


    const handleChange = (e) => {
        const { name, value } = e.target;
        setNuevoEvento((prev) => ({ ...prev, [name]: value }));
    };

    //publicamos un evento para externos, voluntarios externos pueden participar
    const handlePublicar = async () => {
        if (
            !nuevoEvento.nombre ||
            !nuevoEvento.descripcion ||
            !nuevoEvento.fechaHora
        ) {
            alert("Completa todos los campos antes de publicar.");
            return;
        }

        const evento = {
            ...nuevoEvento,
            idEvento: crypto.randomUUID(),
            fechaHora: new Date(nuevoEvento.fechaHora).toISOString(),
        };

        try {
            await publicarEvento(evento);
            alert("Evento publicado correctamente");
            setNuevoEvento({
                idOrganizacion: "ONG001",
                idEvento: "",
                nombre: "",
                descripcion: "",
                fechaHora: "",
            });
        } catch (err) {
            console.error("Error al publicar evento:", err);
            alert("Error al publicar el evento");
        }
    };

    const handleBaja = async (idEvento) => {
        try {
            await darBajaEvento(idEvento);
            alert("Evento dado de baja correctamente");
        } catch (err) {
            console.error("Error al dar de baja evento:", err);
        }
    };

    useEffect(() => {
        const fetchEventos = async () => {
            const externos = await listarEventosExternos();
            setEventos(externos);
            const propios = await listarEventosPropios();
            setMisEventos(propios);
        };
        fetchEventos();
        const interval = setInterval(fetchEventos, 5000);
        return () => clearInterval(interval);
    }, []);

    const [eventosAdheridos, setEventosAdheridos] = useState([]);
    const handleAdherirse = async (evento) => {
        if (eventosAdheridos.includes(evento.idevento)) return;

        const adhesion = {
            idEvento: evento.idevento,
            idOrganizador: evento.idorganizacion,
            voluntario: {
                idOrganizacion: usuarioLogueado.idOrganizacion || "ONG001",
                idVoluntario: usuarioLogueado.id.toString(),
                nombre: usuarioLogueado.nombre,
                apellido: usuarioLogueado.apellido,
                telefono: usuarioLogueado.telefono || "Sin teléfono",
                email: usuarioLogueado.email,
            },
        };

        try {
            await notificarAdhesion(adhesion);
            alert(`${usuarioLogueado.nombre} se unió al evento "${evento.nombre}"`);
        } catch (err) {
            console.error("Error al unirse al evento:", err);
            alert("Error al unirse al evento");
        }
        setEventosAdheridos((prev) => [...prev, evento.idevento]);
    };

    return (
        <div className="main-container">
            <h2>Eventos Externos</h2>

            <div className="input-row">
                <input
                    type="text"
                    name="nombre"
                    placeholder="Nombre del evento"
                    value={nuevoEvento.nombre}
                    onChange={handleChange}
                />
                <input
                    type="text"
                    name="descripcion"
                    placeholder="Descripción"
                    value={nuevoEvento.descripcion}
                    onChange={handleChange}
                />
                <input
                    type="datetime-local"
                    name="fechaHora"
                    value={nuevoEvento.fechaHora}
                    onChange={handleChange}
                />
                <button className="style-btn" onClick={handlePublicar}>Publicar evento</button>
            </div>
            <h3>Mis eventos</h3>
            <ul>
                {misEventos.map((e) => (
                    <li className="list-solicitudes" key={e.idevento}>
                        <b>{e.nombre}</b> — {e.descripcion}
                        <br />
                        Fecha:{" "}
                        {e.fechahora
                            ? new Date(e.fechahora).toLocaleString("es-AR", {
                                day: "2-digit",
                                month: "2-digit",
                                year: "numeric",
                                hour: "2-digit",
                                minute: "2-digit"
                            })
                            : "Sin fecha"}
                        <br />
                        <button className="style-btn" onClick={() => handleBaja(e.idevento)}>Dar de baja</button>
                    </li>
                ))}
            </ul>

            <h3>Eventos de otras ONG's</h3>
            <ul>
                {eventos.map((e) => (
                    <li className="list-solicitudes"
                        key={`${e.idevento}-${e.idorganizacion}`}
                    >
                        <b>({e.idorganizacion}) {e.nombre}</b> — {e.descripcion}
                        <br />
                        Fecha:{" "}
                        {e.fechahora
                            ? new Date(e.fechahora).toLocaleString("es-AR", {
                                day: "2-digit",
                                month: "2-digit",
                                year: "numeric",
                                hour: "2-digit",
                                minute: "2-digit"
                            })

                            : "Sin fecha"}
                        <br />
                        <button
                            className="style-btn"
                            onClick={() => handleAdherirse(e)}
                            disabled={eventosAdheridos.includes(e.idevento)}
                        >
                            {eventosAdheridos.includes(e.idevento)
                                ? "Ya estás participando"
                                : "Unirse al evento"}
                        </button>

                    </li>
                ))}
            </ul>
        </div>
    );
}
