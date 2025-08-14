import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";

import CreateCustomerDrawer from "./components/CreateCustomerDrawer.jsx";

import {
    Spinner,
    Text,
    Wrap,
    WrapItem
} from "@chakra-ui/react";

import CartWithImage from "./components/Cart.jsx";
import {errorNotification} from "./services/notification.js";

function App() {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchCustomers = () => {
        setLoading(true)
        setTimeout(() =>
            getCustomers().then((response) => {
                setCustomers(response.data);
            }).catch((error) => {
                errorNotification(
                    error.code,
                    error.response.data.message,
                );
            }).finally(() => {
                setLoading(false);
            }), 2000
        )

    }

    useEffect(() => {
        fetchCustomers()

    }, [])

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if (customers.length <= 0) {
        return (<SidebarWithHeader>
            <Text>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
            </Text>
            <Text textAlign='center'>There is no customer found</Text>
        </SidebarWithHeader>)
    }

    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer
                fetchCustomers={fetchCustomers}
            />
            <Wrap spacing='30px' justify='center'>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CartWithImage
                            {...customer}
                            imageNumber={index}
                            fetchCustomers={fetchCustomers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App
