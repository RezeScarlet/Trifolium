import * as React from "react"
import {
  MoneyReceiveFlow02Icon,
  TransactionHistoryIcon,
  ShoppingBag01Icon,
  Settings01Icon,
  QuestionIcon,
  ChatFeedback01Icon

} from "hugeicons-react"

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarGroup,
  SidebarGroupLabel,
  SidebarGroupContent,
} from "@/components/ui/sidebar.tsx"

import LogoSVG from "@/assets/Trifolium_no_text.svg"

const data = {
  navMain: [
    {
      title: "Transactions",
      url: "#",
      icon: MoneyReceiveFlow02Icon,
      isActive: true,
    },
    {
      title: "History",
      url: "#",
      icon: TransactionHistoryIcon,
    },
    {
      title: "Categories",
      url: "#",
      icon: ShoppingBag01Icon,
    },
    {
      title: "Settings",
      url: "#",
      icon: Settings01Icon,
    },
  ],
  navSecondary: [
    {
      title: "Support",
      url: "#",
      icon: QuestionIcon,
    },
    {
      title: "Feedback",
      url: "#",
      icon: ChatFeedback01Icon,
    },
  ],
}

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  return (
      <Sidebar collapsible="icon" {...props}>
        <SidebarHeader>
          <SidebarMenu>
            <SidebarMenuItem>
              <SidebarMenuButton size="lg" asChild>
                <a href="#">
                  <div className="flex aspect-square size-10 items-center justify-center rounded-lg  text-sidebar-primary-foreground">
                    <img src={LogoSVG}  alt="Trifolium Clover Logo"/>
                  </div>
                  <div className="grid flex-1 text-left text-sm leading-tight">
                    <span className="font-semibold">Trifolium</span>
                  </div>
                </a>
              </SidebarMenuButton>
            </SidebarMenuItem>
          </SidebarMenu>
        </SidebarHeader>

        <SidebarContent>
          <SidebarGroup>
            <SidebarGroupLabel>Pages</SidebarGroupLabel>
            <SidebarGroupContent>
              <SidebarMenu>
                {data.navMain.map((item) => (
                    <SidebarMenuItem key={item.title}>
                      <SidebarMenuButton tooltip={item.title}>
                        {item.icon && <item.icon />}
                        <span>{item.title}</span>
                      </SidebarMenuButton>
                    </SidebarMenuItem>
                ))}
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
        </SidebarContent>

        <SidebarFooter>
          <SidebarGroup>
            <SidebarGroupContent>
              <SidebarMenu>
                {data.navSecondary.map((item) => (
                    <SidebarMenuItem key={item.title}>
                      <SidebarMenuButton asChild size="sm" tooltip={item.title}>
                        <a href={item.url}>
                          <item.icon />
                          <span>{item.title}</span>
                        </a>
                      </SidebarMenuButton>
                    </SidebarMenuItem>
                ))}
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
        </SidebarFooter>
      </Sidebar>
  )
}