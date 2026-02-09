import { createBrowserRouter } from "react-router-dom";
import Login from "@/pages/Login.tsx";
import AppLayout from "@/layouts/AppLayout";
import Dashboard from "@/pages/Dashboard";

export const router = createBrowserRouter([
    {
        path: "/login",
        element: <Login />,
    },
    {
        path: "/",
        element: <AppLayout />, // Parent Route!!!
        children: [
            {
                index: true,
                element: <Dashboard />
            },
        ],
    },
]);