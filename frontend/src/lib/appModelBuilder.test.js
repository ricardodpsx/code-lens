import {createApp} from "./appModelBuilder";
import {resolvePromises} from "../appModel.test";

describe("redux app model", () => {

  it('Can make a simple app model', () => {

    let appModelDef = {
      gretting: {
        $default: {name: "hello world!"},
        greet: {
          reducer: (name, {}, {}) => ({name: `hello ${name}!`})
        }
      }
    }

    let {store, model} = createApp(appModelDef)

    expect(store.getState().gretting.name).toEqual("hello world!")

    model.gretting.greet("Ricardo")

    expect(store.getState().gretting.name).toEqual("hello Ricardo!")
  })

  it('Can use effects', () => {
    let sideEffect = {}
    let appModelDef = {
      gretting: {
        $default: {text: "hello world!"},
        greet: {
          reducer: (name) => ({name: `hello ${name}!`}),
          effects: [name => sideEffect.x = name + "'s effect"]
        }
      }
    }

    let {model} = createApp(appModelDef)
    model.gretting.greet("Ricardo")
    expect(sideEffect.x).toEqual("Ricardo's effect")
  })


  it('Can communicate between models', () => {
    let firstContact = {name: "Emergency", phone: "211"}
    let addedContact = {name: "Ricardo", phone: "666"}

    let appModelDef = {
      contacts: {
        $default: {selected: -1, list: [firstContact]},
        addContact: (contact, {list}) => ({list: list.concat(contact)}),
        select: selected => ({selected})
      },
      contactForm: {
        $default: null,
        updateContact: ({name, phone}) => ({name, phone}),
        save: {
          effects: (_, {contacts: {addContact}, contactForm: {name, phone}}) => addContact({name, phone})
        },
        $when: {contacts: {select: (selected, {contactForm: {updateContact}, contacts: {list}}) => updateContact(list[selected])}}
      }
    }

    let {model: {contactForm, contacts}} = createApp(appModelDef)
    contactForm.updateContact(addedContact)

    //Updating list of contacts
    expect(contacts.list).toEqual([firstContact])
    contactForm.save()
    expect(contacts.list).toContainEqual(addedContact)


    //Selecting a contact
    contacts.select(0)
    console.info(contactForm.toString())
    expect(contactForm.name).toEqual(firstContact.name)
    expect(contactForm.phone).toEqual(firstContact.phone)
  })
})