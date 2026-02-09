import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Input} from "@/components/ui/input.tsx";

const Login: React.FC = () => {
    return (
        <>
            <div className="flex min-h-screen flex-col items-center justify-center gap-6 bg-gray-100 p-4 dark:bg-zinc-900">

                <h1 className="text-2xl font-bold">Vite + React + Shadcn</h1>

                <Card className="w-full max-w-sm">
                    <CardHeader className="relative">
                        <div className="flex items-center justify-between">
                            <CardTitle>Login</CardTitle>
                            <Button variant="link" className="h-auto p-0 text-sm">
                                Sign Up
                            </Button>
                        </div>
                        <CardDescription>
                            Entre com seu email para acessar
                        </CardDescription>
                    </CardHeader>

                    <form onSubmit={(e) => { e.preventDefault(); alert("Login enviado!"); }}>
                        <CardContent>
                            <div className="flex flex-col gap-6">
                                <div className="grid gap-2">
                                    <Label htmlFor="email">Email</Label>
                                    <Input
                                        id="email"
                                        type="email"
                                        placeholder="m@example.com"
                                        required
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <div className="flex items-center">
                                        <Label htmlFor="password">Senha</Label>
                                        <a
                                            href="#"
                                            className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                                        >
                                            Esqueceu a senha?
                                        </a>
                                    </div>
                                    <Input id="password" type="password" required />
                                </div>
                            </div>
                        </CardContent>

                        <CardFooter className="flex flex-col gap-2">
                            <Button type="submit" className="w-full">
                                Login
                            </Button>
                            <Button variant="outline" className="w-full" type="button">
                                Login with Google
                            </Button>
                        </CardFooter>
                    </form>
                </Card>
            </div>
        </>
    )
}

export default Login